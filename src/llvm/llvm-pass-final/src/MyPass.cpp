#define DEBUG_TYPE "simpleDeadCodeEliminator"
#include "llvm/Pass.h"
#include "llvm/IR/Function.h"
#include "llvm/Support/raw_ostream.h"
#include <vector>
#include <map>
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
    set<Instruction *> liveIn;
    set<Instruction *> liveOut;
  };
  static char ID;
  FinalDCE() : FunctionPass(ID) {}
  bool runOnFunction(Function &F) override
  {
    //errs() << "Function " << F.getName() << '\n';

    map<BasicBlock *, LiveSets> initialAnalysis = DoLivenessAnalysis(F);
    printLiveness(F, initialAnalysis);
    bool removed = false;
    do
    {
      //do the liveness analysis

      removed = false;
      vector<Instruction *> workList;
      workList.clear();
      //go through each instruction
      map<BasicBlock *, LiveSets> analysis = DoLivenessAnalysis(F);
      for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb)
      {
        for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i)
        {
          //i is an instruction
          //remove it if it is dead
          bool isDead = true;
          Instruction *instr = &(*i);
          //instruction is dead if it exists in no live-out sets
          for (auto const &x : analysis)
          {
            set<Instruction *> l = x.second.liveOut;
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
            removed = true;
          }
        }
      }

      //remove dead instructions
      errs() << "Final DCE: Removed " << workList.size() << " instruction(s)\n";
      for (int j = 0; j < workList.size(); ++j)
      {
        workList[j]->eraseFromParent();
      }

    } while (removed);

    return false;
  }

  //does the liveness analysis, returns set of basic blocks with thier live-in and live-out sets
  map<BasicBlock *, LiveSets> DoLivenessAnalysis(Function &F)
  {
    map<BasicBlock *, LiveSets> analysis;
    //initialise live-in and live-out sets
    for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb)
    {
      //for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i)
      //{
      //Instruction *instr = &(*i);
      analysis[&(*bb)] = {};
      //}
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
          for (int v = 0; v < instr->getNumOperands(); v++)
          {
            Instruction *variable = dyn_cast<Instruction>(instr->getOperand(v));
            if (variable != nullptr)
            {
              int origSize = analysis[block].liveIn.size();
              analysis[block].liveIn.insert(variable);
              if (origSize < analysis[block].liveIn.size()) //i.e it actually added something
              {
                change = true;
              }
            }
          }
        }

        //all-live-in variables in this block are live-out in prdecessors
        for (auto i = analysis[block].liveIn.begin(), e = analysis[block].liveIn.end();i!=e;i++)
        {
          for (auto it = pred_begin(block), et = pred_end(block); it != et; ++it)
          {
            BasicBlock *pred = *it;

            int origSize = analysis[pred].liveOut.size();
            analysis[pred].liveOut.insert(*i);
            if (origSize < analysis[pred].liveOut.size())
            {
              change = true;
            }
          }
        }

        //if a variable is live-out and not defined here, then it is also live-in
        for (auto i = analysis[block].liveOut.begin(), ed = analysis[block].liveOut.end();i!=ed;i++)
        {
          bool definedInBlock = false;
          for (BasicBlock::iterator ii = bb->begin(), e = bb->end(); ii != e; ++ii)
          {
            Instruction *instr = &(*ii);
            if (instr == *i)
            {
              definedInBlock = true;
              break;
            }
          }
          if (!definedInBlock)
          {
             int origSize = analysis[block].liveIn.size();
             analysis[block].liveIn.insert(*i);
            if(origSize < analysis[block].liveIn.size()){
              change = true;
            }
          }
        }
      }
      //printLiveness(F, analysis);
    } while (change);

    return analysis;
  }

  void printLiveness(Function &F, map<BasicBlock *, LiveSets> analysis)
  {
    for (Function::iterator bb = F.begin(), end = F.end(); bb != end; bb++)
    {
      for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; i++)
      {
        // skip phis
        if (dyn_cast<PHINode>(i))
          continue;

        errs() << "{";

        auto operatorSet = analysis[&(*bb)].liveIn;
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

static RegisterStandardPasses Y(
    PassManagerBuilder::EP_EarlyAsPossible,
    [](const PassManagerBuilder &Builder,
       legacy::PassManagerBase &PM) { PM.add(new FinalDCE()); });
