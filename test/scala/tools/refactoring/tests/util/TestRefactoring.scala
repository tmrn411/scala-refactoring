package scala.tools.refactoring.tests.util

import scala.tools.refactoring.analysis.FullIndexes
import scala.tools.refactoring.Refactoring
import scala.tools.refactoring.common.Change
import org.junit.Assert._

trait TestRefactoring {
  
  self: TestHelper =>
   
  abstract class TestRefactoringImpl(project: FileSet) {
      
    val refactoring: Refactoring with FullIndexes
    
    @Deprecated
    def doIt(expected: String, parameters: refactoring.RefactoringParameters) = {
      val result = performRefactoring(parameters)
      assertEquals(expected, applyChangeSet(result, project.sources.head))
    }
    
    def performRefactoring(parameters: refactoring.RefactoringParameters): List[Change] = {

      val selection = new refactoring.FileSelection(project.selection.file, project.selection.pos.start, project.selection.pos.end)
      
      refactoring.prepare(selection) match {
        case Right(prepare) =>
          refactoring.perform(selection, prepare, parameters) match {
            case Right(result) => result
            case Left(error) => fail(error.cause); throw new Exception("Unreachable, :-/ Java type system")
          }
        case Left(error) => fail(error.cause); throw new Exception("Unreachable, :-/ Java type system")
      }
    }
  }
}
