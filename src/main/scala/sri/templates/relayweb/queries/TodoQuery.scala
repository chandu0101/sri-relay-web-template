package sri.templates.relayweb.queries

import sri.relay.container.RelayContainer.RootQueries
import sri.relay.container.RootQueries
import sri.relay.query.RelayQL
import sri.relay.route.RelayQueryConfig
import sri.relay.route.RelayQueryConfig.Params

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}

object TodoQuery {

  def apply() = new RelayQueryConfig {
    override val queries: RootQueries = RootQueries("viewer" -> (() => js.eval(RelayQL( """query { viewer}"""))))
    override val name: String = "TodoQuery"
    override val params: Params = js.Dictionary()
  }
}
