package dal.actors

import akka.actor.{Props, DiagnosticActorLogging, Actor}
import com.typesafe.cinnamon.akka.Tracer
import dal.actors.PersonRepoActor.RepoPersons

class PrintNewPersonActor extends Actor with DiagnosticActorLogging {

  private val tracer = Tracer(context.system)

  def receive = {
    case RepoPersons(persons) =>
      log.info("persons updated")
      tracer.end("create-person")
  }
}

object PrintNewPersonActor {

  def props = Props(new PrintNewPersonActor)
}
