package models

import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import com.typesafe.config.{Config, ConfigFactory}

class MongodbClient {
  private lazy val config: Config = ConfigFactory.load()
  private lazy val dbName: String = config.getString("mongo.database").trim
  private lazy val uri: String = config.getString("mongo.uri").trim
  private lazy val username: String = config.getString("mongo.credentials.username").trim
  private lazy val password: String = config.getString("mongo.credentials.password").trim
  private lazy val domain: String = uri.substring(10)
  private lazy val mongoURI = s"mongodb://$username:$password@$domain".replace("mongodb://:@", "mongodb://")
  def client: MongoClient = MongoClient(mongoURI)

  lazy val codecRegistry = fromRegistries(fromProviders(classOf[TaskDAO]), DEFAULT_CODEC_REGISTRY)

  lazy val database: MongoDatabase = client.getDatabase(dbName).withCodecRegistry(codecRegistry)

  object Collection {
    lazy val tasks: String = config.getString("mongo.collection.tasks")
  }

    def taskCollection(db: String = dbName, collection: String = Collection.tasks): MongoCollection[TaskDAO] =
      database.getCollection[TaskDAO](collection)
}

sealed trait DbException
object DbException {
  case object NotFound extends DbException
  case object ParsingFailure extends DbException
  case object Timeout extends DbException
  case object SocketTimeout extends DbException
  case class ThrowException(e: Exception) extends DbException
}
