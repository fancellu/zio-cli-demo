import zio.cli.HelpDoc.Span.text
import zio.{ZIO, ZIOAppArgs, ZIOAppDefault}
import zio.cli.*
import zio.Console.*
import zio.cli.ValidationErrorType.InvalidValue

object ZIOCLI extends ZIOAppDefault:

  sealed trait Subcommand extends Product with Serializable
  object Subcommand:
    final case class Add() extends Subcommand
    final case class AddNumber(bi: BigInt) extends Subcommand
    final case class AddName(name: String, flag: Boolean) extends Subcommand
    object SubSub:
      sealed trait SubSubCommand extends Subcommand
      final case class One(name: String) extends SubSubCommand
      final case class Two(name: String) extends SubSubCommand

  val add =
    Command("add")
      .withHelp(HelpDoc.p("Runs the add command, takes no params"))
      .map(_ => Subcommand.Add())

  private val addNumber =
    Command("addNumber", Args.integer)
      .withHelp(
        HelpDoc.p(
          "Runs the addNumber command takes an integer, must be 10 or less"
        )
      )
      .map(bi => Subcommand.AddNumber(bi))

  private val oneAdd =
    Command("one", Options.text("name"))
      .withHelp(HelpDoc.p("Add one subcommand description"))
      .map(name => Subcommand.SubSub.One(name))

  private val twoAdd =
    Command("two", Options.text("name").alias("n"))
      .withHelp(HelpDoc.p("Add two subcommand description"))
      .map {
        Subcommand.SubSub.Two.apply
      }

  private val addName =
    Command("addName", Options.boolean("flag").alias("f"), Args.text)
      .withHelp(
        HelpDoc.p(
          "Runs the addName command takes a name param and optional flag"
        )
      )
      .map { case (flag, text) =>
        Subcommand.AddName(text, flag)
      }

  private val subSub =
    Command("subSub")
      .withHelp(
        HelpDoc.p("Runs the subsub command and takes subcommands one and two")
      )
      .subcommands(oneAdd, twoAdd)

  private val ziocli: Command[Subcommand] =
    Command("ziocli", Options.none, Args.none).subcommands(
      add,
      addName,
      subSub,
      addNumber
    )

  private val cliApp = CliApp.make(
    "ziocli",
    "0.1",
    summary = text("a sample zio-cli app"),
    command = ziocli
  ) {
    case Subcommand.Add() => printLine("add").unit
    case Subcommand.AddName(name, flag) =>
      printLine(s"addName $name FLAG=$flag").unit
    case Subcommand.SubSub.One(name) =>
      printLine(s"addName.one $name").unit
    case Subcommand.SubSub.Two(name) =>
      printLine(s"addName.two $name").unit
    case Subcommand.AddNumber(bi) =>
      if bi > 10 then
        ZIO.fail(
          ValidationError(
            validationErrorType = InvalidValue,
            error = HelpDoc.p(
              s"$bi is too big, must be <=10"
            )
          )
        )
      else printLine(s"addNumber $bi").unit
  }

  // can't use ZIOCliDefault as it doesn't allow us to catchAll
  // the default is to throw a stack trace if an error occurs
  override def run =
    for
      args <- ZIOAppArgs.getArgs
      _ <-
        printLine(s"""Args=${args.mkString(",")}""").unless(args.isEmpty)
      result <- cliApp.run(args.toList).catchAll {
        case e: ValidationError =>
          printLineError(s"Error: ${e.error.toPlaintext()}").unit
        case e => printLineError(s"Unexpected error: $e").unit
      }
    yield result
