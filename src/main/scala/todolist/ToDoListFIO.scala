package todolist

import scalaz._
import effect._
import IO._
import IOFunctions._

/**
 * A “functional programming” (FP) style To-Do List application,
 * with some “functional I/O.”
 */
object ToDoListFIO extends App {

    /**
     * Note that datafile is used as a closured-thing. Not horrible in this situation.
     */
    val datafile = "/Users/al/Projects/Scala/FunctionalProgramming/ToDoList2/todo.dat"
    val prompt = "Command ('h' for help, 'q' to quit)\n==>"

    /**
     * As its name implies, this is the main loop.
     * See the `help` function for a list of things the user can type in.
     */
    def mainLoop: IO[Unit] = for {
        _     <- IOFunctions.putStr(prompt)
        cmd   <- getLine.map(Command.parse _)
        _     <- if (cmd == Quit) {
                     IO()
                 } else {
                     processCommand(cmd).unsafePerformIO
                     mainLoop  //note the recursion here
                 }
    } yield ()

    // with scalaz, this starts the loop
    mainLoop.unsafePerformIO

    /**
     * got a command from the user, handle it
     */
    def processCommand(cmd: Command): IO[Unit] = cmd match {
        case Add(task) => {
            add(task).unsafePerformIO
            view
        }
        case Remove(n) => {
            // TODO this will eventually fail if a number is not supplied after 'remove'
            remove(n.toInt).unsafePerformIO
            view
        }
        case View => {
            view
        }
        case Help => {
            help
        }
        case Unknown => {
            IO(println("Unknown (sorry, i don’t know what you want)"))
        }
    }

    /**
     * append the task to the file
     */
    def add(task: String): IO[Unit] = writeFile(datafile, task, true)
    
    /**
     * list all of the items in the file, with the task number before each task.
     */
    def view: IO[Unit] = IO {
        val lines = readFile(datafile).unsafePerformIO
        val result = (for ((line,i) <- lines.zip(Stream from 1)) yield s"$i. $line").mkString("\n")
        println(result + "\n")
    }

    def help: IO[Unit] = IO {
        val text = """
        |Possible commands
        |-----------------
        |add <task>       - add a to-do item
        |h                - show this help text
        |rm [task number] - remove a task by its number
        |v                - view the list of tasks
        |q                - quit
        """.stripMargin
        println(text)
    }

    /**
     * remove the given task number from the file.
     * `taskToRemove` will be based on 1,2,3.
     * however, the list will be zero-based.
     */
    def remove(taskNumToRemove: Int) = {
        val currentTasks = readFile(datafile).unsafePerformIO.toVector
        val remainingTasks = CollectionUtils.removeElementFromSequence(currentTasks, taskNumToRemove-1)
        val remainingTasksAsString = remainingTasks.mkString("\n")
        writeFile(datafile, remainingTasksAsString, false)
    }

    private def getArgs: IO[Tuple2[String, String]] = {
        IO(args(0), args(1))
    }

}





