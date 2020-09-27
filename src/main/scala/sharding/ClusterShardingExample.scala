package sharding

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}

object ClusterShardingExample extends App {
  val system = ActorSystem(Behaviors.empty, "system")
  val logger = system.systemActorOf(Behaviors.logMessages(Behaviors.ignore[Any]), "logger")

  val clusterSharding = ClusterSharding(system)

  val TypeKey = EntityTypeKey[Counter.Command]("counter")
  val shardRegion =
    clusterSharding.init(Entity(TypeKey) { context =>
      Counter(context.entityId)
    })

  val counterOne = clusterSharding.entityRefFor(TypeKey, "counter-1")
  counterOne ! Counter.Increment
  counterOne ! Counter.GetValue(logger)

  shardRegion ! ShardingEnvelope("counter-1", Counter.Increment)
  shardRegion ! ShardingEnvelope("counter-1", Counter.GetValue(logger))
}
