import com.google.inject.AbstractModule
import com.typesafe.config.{Config, ConfigFactory}

class Module extends AbstractModule {
  lazy val config: Config = ConfigFactory.load()

  @Override
  override def configure(): Unit = {
    bind(classOf[Config]).toInstance(config)
    bind(classOf[Boot]).asEagerSingleton()
  }
}
