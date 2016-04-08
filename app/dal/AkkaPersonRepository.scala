package dal

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import akka.actor.ActorSystem
import akka.util.Timeout
import com.google.inject.Singleton
import com.typesafe.cinnamon.akka.Tracer
import dal.actors.PersonRepoActor
import dal.actors.PersonRepoActor.{RepoPersons, GetPersons, PersonCreated, CreatePerson}
import models.Person

import scala.concurrent.Future

@Singleton
class AkkaPersonRepository @Inject() (system: ActorSystem) {
  import akka.pattern.ask
  implicit val to = Timeout.apply(1, TimeUnit.SECONDS)
  import system.dispatcher

  private val tracer = Tracer(system)
  private val personRepo = system.actorOf(PersonRepoActor.props, "person-repo")

  def create(name: String, age: Int): Future[Person] = {
    tracer.start("create-person"){
      personRepo.ask(CreatePerson(name, age)).map {
        case PersonCreated(person) => person
      }
    }
  }

  def list(): Future[Seq[Person]] = personRepo.ask(GetPersons).map {
    case RepoPersons(persons) => persons
  }
}
