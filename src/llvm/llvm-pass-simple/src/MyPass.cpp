// Example of how to write an LLVM pass
// For more information see: http://llvm.org/docs/WritingAnLLVMPass.html
/*
#include "llvm/Pass.h"
#include "llvm/IR/Function.h"
#include "llvm/Support/raw_ostream.h"

#include "llvm/IR/LegacyPassManager.h"
#include "llvm/Transforms/IPO/PassManagerBuilder.h"
http://snappytomatopizza.com/
using namespace llvm;

namespace {
struct MyPass : public FunctionPass {
  static char ID;
  MyPass() : FunctionPass(ID) {}

  bool runOnFunction(Function &F) override {
    errs() << "I saw a function called " << F.getName() << "!\n";
    return false;
  }
};
}

char MyPass::ID = 0;
static RegisterPass<MyPass> X("mypass", "My own pass");

static RegisterStandardPasses Y(
    PassManagerBuilder::EP_EarlyAsPossible,
    [](const PassManagerBuilder &Builder,
       legacy::PassManagerBase &PM) { PM.add(new MyPass()); });

*/
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
struct SimpleDCE : public FunctionPass
{
  static char ID;
  SimpleDCE() : FunctionPass(ID) {}
  bool runOnFunction(Function &F) override
  {
    errs() << "Function " << F.getName() << '\n';

    bool removed = false;
    do
    {
      removed = false;
      //TODO: free up worklist first to avoid memory leaks?
      vector<Instruction*> workList;
      workList.clear();
      //go through each instruction
      for (Function::iterator bb = F.begin(), e = F.end(); bb != e; ++bb)
      {
        for (BasicBlock::iterator i = bb->begin(), e = bb->end(); i != e; ++i)
        {
          //i is an instruction
          //remove it if it is dead
          if (llvm::isInstructionTriviallyDead(&(*i),nullptr))
          
          //if (true)
          {
            workList.push_back(&(*i));
            removed = true;
          }
        }
      }

      //remove dead instructions
      errs() << "Removed " << workList.size() << " instruction(s)\n";
      for (int j = 0; j < workList.size(); ++j)
      {
        workList[j]->eraseFromParent();
      }

    } while (removed);

    return false;
  }
};
} // namespace
char SimpleDCE::ID = 0;
static RegisterPass<SimpleDCE> X("mypass", "My pass : Elminates dead code");

static RegisterStandardPasses Y(
    PassManagerBuilder::EP_EarlyAsPossible,
    [](const PassManagerBuilder &Builder,
       legacy::PassManagerBase &PM) { PM.add(new SimpleDCE()); });
