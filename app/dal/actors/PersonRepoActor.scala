package dal.actors

import akka.actor.{Actor, DiagnosticActorLogging, Props, Status}
import dal.actors.PersonRepoActor.{CreatePerson, GetPersons, PersonCreated, RepoPersons}
import models.Person
import org.slf4j.MDC

class PersonRepoActor extends Actor with DiagnosticActorLogging {

  var currentPersonNr = 0L
  var persons: List[Person] = List.empty[Person]

  val printer = context.actorOf(PrintNewPersonActor.props, "printer")

  def receive = {
    case CreatePerson("mrerror", _) =>
      log.error("mrerror is a bad person, current persons {}", persons)
      sender() ! Status.Failure(new IllegalArgumentException("mrerror is not vaild"))

    case CreatePerson(name, age) =>
      val person = Person(newPersonNr(), name, age)
      MDC.put("pnr", String.valueOf(person.id))
      persons = person +: persons
      log.info("person persisted")
      sender() ! PersonCreated(person)
      printer ! RepoPersons(persons)

    case GetPersons =>
      sender() ! RepoPersons(persons)
  }

  def newPersonNr() = {
    currentPersonNr = currentPersonNr + 1
    currentPersonNr
  }
}

object PersonRepoActor {

  def props = Props(new PersonRepoActor)

  case class CreatePerson(name: String, age: Int)

  case class PersonCreated(person: Person)

  case object GetPersons

  case class RepoPersons(persons: List[Person])
}
