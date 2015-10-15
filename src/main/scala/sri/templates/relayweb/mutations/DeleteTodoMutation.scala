package sri.templates.relayweb.mutations

import org.scalajs.dom
import sri.relay.mutation.RelayMutation
import sri.relay.query.RelayQL
import sri.relay.tools.RelayTypes.MutationFragment

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js._
import scala.scalajs.js.annotation.ScalaJSDefined

@ScalaJSDefined
class DeleteTodoMutation(input: js.Dynamic) extends RelayMutation(input) {

  override def getMutation(): MutationFragment = js.eval(RelayQL( """mutation { deleteTodo}"""))

  override def getVariables(): Object = json(id = props.id)

  override def getFatQuery(): Any = js.eval(RelayQL(
    """
           fragment on _TodoPayload {
              id,
              viewer {
                id,
                allTodos {
                  count,
                }
              }
            }
    """))

  override def getConfigs(): Array[Dictionary[Any]] = js.Array(
    js.Dictionary("type" -> "NODE_DELETE", "parentName" -> "viewer", "parentID" -> props.viewer.id, "connectionName" -> "allTodos", "deletedIDFieldName" -> "id"),
    js.Dictionary("type" -> "FIELDS_CHANGE", "fieldIDs" -> js.Dictionary("viewer" -> props.viewer.id))
  )

  override def getOptimisticResponse(): UndefOr[Object] = {
    println(s"delete optimistic response props")
    dom.window.console.log(props)
    json(id = props.id,
      viewer = json(id = props.viewer.id, allTodos = json(count = props.viewer.allTodos.count.asInstanceOf[Int] - 1)))
  }
}
