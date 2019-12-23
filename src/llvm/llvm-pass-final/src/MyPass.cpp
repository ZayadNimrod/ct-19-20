#define DEBUG_TYPE "simpleDeadCodeEliminator"
#include "llvm/Pass.h"
#include "llvm/IR/Function.h"
#include "llvm/Support/raw_ostream.h"
#include <vector>

#include "llvm/IR/LegacyPassManager.h"
#include "llvm/Transforms/IPO/PassManagerBuilder.h"
#include "llvm/Transforms/Utils/Local.h"
using namespace llvm;
using namespace std;

namespace
{
struct FinalDCE : public FunctionPass
{
  static char ID;
  FinalDCE() : FunctionPass(ID) {}
  bool runOnFunction(Function &F) override
  {
    errs() << "Function " << F.getName() << ":\n";

    bool removed = false;
    do
    {

      removed = false;

      vector<Instruction *> unused;
      unused.clear();
      //set up unused array, all instructions are considered unused until they prove otherwise
      for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb)
      {
        for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i)
        {
          Instruction *instr = &*i;
          unused.push_back(instr);
        }
      }

      //go through each instruction
      //check operands, mark them as used
      for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb)
      {

        for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i)
        {

          Instruction *instr = &*i;
          errs() << "| " << *instr << " \n";
          //get the operands
          int numOps = instr->getNumOperands();
          if (numOps == 0 || numOps == 1)
          {
            //either way, this is never "used", but is probably important, so this should not be removed
            //errs() << "No operands!\n";
            unused.erase(remove(unused.begin(), unused.end(), instr), unused.end());
          }
          for (int j = 0; j < numOps; j++)
          {
            Value *op = instr->getOperand(j);
            //check if op is a variable, if so, remove from unused
            //Instruction *reg = (dynamic_cast<Instruction*>(op));

            //errs() << "? " << *op << " \n";
            if (find(unused.begin(), unused.end(), op) != unused.end())
            {
              //the operand is a)an Instruction and b)has not already been used (if it has then it doesn't really matter who uses it)
              unused.erase(remove(unused.begin(), unused.end(), op), unused.end());
            }
          }
        }
      }

      //remove dead instructions
      errs() << "Removed " << unused.size() << " instruction(s)\n";
      //Go through each intruction, delete the ones that are never unused later
      for (int j = 0; j < unused.size(); ++j)
      {
        //evrything gets put here...?
        //we're removing Ret??
        errs() << "removed " << *(unused[j]) << " \n";
        unused[j]->eraseFromParent();
        removed = true;
      }

    } while (removed);
    errs() << "Finished Dead Code Elimination!\n";
     for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb)
      {
        for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i)
        {
          Instruction *instr = &*i;
          errs() << "" << *instr << " \n";
        }
      }
    return false;
  }
};
} // namespace
char FinalDCE::ID = 0;
static RegisterPass<FinalDCE> X("mypass", "My pass : Elminates dead code");

static RegisterStandardPasses Y(
    PassManagerBuilder::EP_EarlyAsPossible,
    [](const PassManagerBuilder &Builder,
       legacy::PassManagerBase &PM) { PM.add(new FinalDCE()); });
