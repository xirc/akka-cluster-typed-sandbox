package singleton

import akka.actor.typed.{ActorSystem, Behavior, SupervisorStrategy}
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.typed.{ClusterSingleton, SingletonActor}

object CounterSingletonExample extends App {
  object Guardian {
    def apply(): Behavior[Counter.Command] = {
      Behaviors.setup { context =>
        val clusterSingleton = ClusterSingleton(context.system)
        val proxy = clusterSingleton.init(
          SingletonActor(
            Behaviors.supervise(Counter())
              .onFailure[Exception](SupervisorStrategy.restart),
            "GlobalCounter"
          )
        )
        Behaviors.logMessages {
          Behaviors.receiveMessage { message =>
            proxy ! message
            Behaviors.same
          }
        }
      }
    }
  }

  val system = ActorSystem(Guardian(), "system")
  val logger = system.systemActorOf(Behaviors.logMessages(Behaviors.ignore[Int]), "logger")

  for (_ <- 1 to 10) {
    system ! Counter.Increment
    system ! Counter.GetValue(logger)
    Thread.sleep(500)
  }
}
