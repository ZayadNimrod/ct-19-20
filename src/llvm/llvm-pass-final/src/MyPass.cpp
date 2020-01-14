#define DEBUG_TYPE "simpleDeadCodeEliminator"
#include "llvm/Pass.h"
#include "llvm/IR/Function.h"
#include "llvm/IR/Instructions.h"
#include "llvm/IR/InstIterator.h"
#include "llvm/Support/raw_ostream.h"
#include <vector>
#include <map>
#include <algorithm>
#include <set>
#include "llvm/IR/LegacyPassManager.h"
#include "llvm/Transforms/IPO/PassManagerBuilder.h"
#include "llvm/Transforms/Utils/Local.h"
using namespace llvm;
using namespace std;

namespace
{
struct FinalDCE : public FunctionPass
{
  struct LiveSets
  {
    set<Value *> liveIn;
    set<Value *> liveOut;
    map<BasicBlock *, set<Value *>> useSets;
  };
  static char ID;
  FinalDCE() : FunctionPass(ID) {}
  bool runOnFunction(Function &F) override
  {
    //errs() << "Function " << F.getName() << '\n';

    map<Instruction *, LiveSets> initialAnalysis = DoLivenessAnalysis(F);
    printLiveness(F, initialAnalysis);
    bool removed = false;
    do
    {
      //do the liveness analysis

      removed = false;
      vector<Instruction *> workList;
      workList.clear();
      //go through each instruction
      map<Instruction *, LiveSets> analysis = DoLivenessAnalysis(F);
      for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb)
      {
        for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i)
        {
          //i is an instruction
          //remove it if it is dead
          bool isDead = true;
          Instruction *instr = &(*i);
          //instruction is dead if it exists in no live-out sets
          for (auto &x : analysis)
          {
            set<Value *> l = x.second.liveOut;
            //if live-out contains instr, we're good
            if (find(l.begin(), l.end(), instr) != l.end())
            {
              isDead = false;
              break;
            }
          }

          if (isDead)

          //if (true)
          {
            workList.push_back(instr);
          }
        }
      }

      //remove dead instructions
      //errs() << "Final DCE: Removed " << workList.size() << " instruction(s)\n";
      for (int j = 0; j < workList.size(); ++j)
      {
        if (!(workList[j])->isTerminator())
        {
          //errs() << *(workList[j]) << "\n";
          workList[j]->eraseFromParent();
          removed = true;
        }
      }

    } while (removed);

    return false;
  }

  //does the liveness analysis, returns set of basic blocks with thier live-in and live-out sets
  map<Instruction *, LiveSets> DoLivenessAnalysis(Function &F)
  {
    map<Instruction *, LiveSets> analysis;
    //initialise live-in and live-out sets
    for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb)
    {
      for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i)
      {
        analysis[&(*i)] = {};
      }
    }

    bool change = false;
    do
    {
      change = false;
      for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb)
      {
        //all variables defined in block are in the live-in set
        BasicBlock *block = &(*bb);
        for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i)
        {

          Instruction *instr = &(*i);
          auto origLiveIn = analysis[instr].liveIn;
          auto origLiveOut = analysis[instr].liveOut;
          auto origUse = analysis[instr].useSets;
          analysis[instr].liveIn.clear();

          //analysis[instr].liveIn.insert(instr);

          set<Value *> defined = {instr};
          set<Value *> use;
          PHINode *phi = dyn_cast<PHINode>(instr);
          if (phi == nullptr)
          {
            for (int j = 0; j < instr->getNumOperands(); j++)
            {
              Value *val = instr->getOperand(j);
              if (isa<Instruction>(val) || isa<Argument>(val))
              {
                use.insert(val);
              }
            }
            BranchInst *branch = dyn_cast<BranchInst>(instr);
            if (branch != nullptr)
            {
              if (branch->isConditional())
              {
                Value *cond = branch->getCondition();
                if (isa<Argument>(cond) || isa<Instruction>(cond))
                {
                  //errs() << "Added conditon:" << *cond << "\n";
                  use.insert(cond);
                }
              }
            }
          }
          else
          {
            for (int j = 0; j < phi->getNumIncomingValues(); j++)
            {
              Value *val = phi->getIncomingValue(j);
              BasicBlock *sourceBlock = phi->getIncomingBlock(j);
              if (isa<Instruction>(val) || isa<Argument>(val))
              {
                analysis[phi].useSets[sourceBlock].insert(val);
              }
            }
            //take into account multiple phi nodes, proogate use sets upwards to top
            Instruction *next = instr->getNextNode();
            if (next != nullptr)
            {
              PHINode *nextPhi = dyn_cast<PHINode>(next);
              while (nextPhi != nullptr)
              {
                //appent use sets for each source block to mine
                for (int j = 0; j < nextPhi->getNumIncomingValues(); j++)
                {
                  Value *nextVal = nextPhi->getIncomingValue(j);
                  BasicBlock *nextSourceBlock = phi->getIncomingBlock(j);
                  if (isa<Instruction>(nextVal) || isa<Argument>(nextVal))
                  {
                    analysis[instr].useSets[nextSourceBlock].insert(nextVal);
                  }
                }
                next = next->getNextNode();
                if (next == nullptr)
                {
                  nextPhi = nullptr;
                }
                else
                {
                  PHINode *nextPhi = dyn_cast<PHINode>(next);
                }
              }
            }
          }
          set<Value *> tempset;
          set_difference(analysis[instr].liveOut.begin(), analysis[instr].liveOut.end(), defined.begin(), defined.end(), inserter(tempset, tempset.begin()));
          set_union(use.begin(), use.end(), tempset.begin(), tempset.end(), inserter(analysis[instr].liveIn, analysis[instr].liveIn.begin()));
          analysis[instr].liveOut.clear();
          //outset of combination of all successor nodes
          vector<BasicBlock *> succs;

          if (!instr->isTerminator())
          {
            Instruction *next = instr->getNextNode();
            for (auto i : analysis[next].liveIn)
              analysis[instr].liveOut.insert(i);
            //analysis[instr].liveOut.insert(next);
          }
          else
          {
            //last inst in basic block, get next block
            int succs = instr->getNumSuccessors();
            for (int k = 0; k < succs; k++)
            {
              BasicBlock *blok = instr->getSuccessor(k);
              //add the first nodes in to my out
              for (auto kk : analysis[&*(blok->begin())].liveIn)
              {
                if (isa<Argument>(kk) || isa<Instruction>(kk))
                {
                  analysis[instr].liveOut.insert(kk);
                }
              }

              if (isa<PHINode>(blok->begin()))
              {

                //if (analysis[&*(blok->begin())].useSets.contains(instr->getParent()))
                {
                  set<Value *> u = analysis[&*(blok->begin())].useSets[instr->getParent()];
                  for (Value *v : u)
                  {
                    if (isa<Argument>(v) || isa<Instruction>(v))
                    {
                      analysis[instr].liveOut.insert(v);
                    }
                  }
                }
              }
            }
          }

          if ((origLiveIn != analysis[instr].liveIn) || origLiveOut != analysis[instr].liveOut)
          {
            change = true;
          }
        }
      }
    } while (change);

    return analysis;
  }

  void
  printLiveness(Function &F, map<Instruction *, LiveSets> analysis)
  {
    for (Function::iterator bb = F.begin(), end = F.end(); bb != end; bb++)
    {
      for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; i++)
      {
        // skip phis
        if (dyn_cast<PHINode>(i))
          continue;

        errs() << "{";

        auto operatorSet = analysis[&(*i)].liveIn;
        for (auto oper = operatorSet.begin(); oper != operatorSet.end(); oper++)
        {
          auto op = *oper;
          if (oper != operatorSet.begin())
            errs() << ", ";
          (*oper)->printAsOperand(errs(), false);
        }

        errs() << "}\n";
      }
    }
    errs() << "{}\n";
  }

}; // namespace

} // namespace
char FinalDCE::ID = 0;
static RegisterPass<FinalDCE> X("mypass", "My pass : Elminates dead code");