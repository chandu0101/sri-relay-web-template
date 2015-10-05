package sri.templates.relayweb.mutations

import sri.relay.mutation.RelayMutation
import sri.relay.query.RelayQL
import sri.relay.tools.RelayTypes.MutationFragment

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{Any, Array, Dictionary, UndefOr}

@ScalaJSDefined
class AddTodoMutation(props : js.Dynamic) extends RelayMutation(props) {

  override def getMutation(): MutationFragment = {
    js.eval(RelayQL( """mutation{ createTodo }"""))
  }

  override def getVariables(): js.Object = json("text" -> props.text.toString,
    "complete" -> false)

  override def getFatQuery(): Any = js.eval(RelayQL(
    """
       fragment on _TodoPayload {
              changedTodoEdge,
              viewer {
                id,
                allTodos {
                  count
                }
              }
            }
    """))

//  override def getOptimisticResponse(): UndefOr[js.Object] = {
//
//    json("changedTodoEdge" -> js.Dictionary("node" -> js.Dictionary("text" -> props.text, "complete" -> false)),
//      "viewer" -> js.Dictionary("id" -> props.viewer.id, "allTodos" -> js.Dictionary("id" -> props.viewer.id, "allTodos" -> js.Dictionary("count" -> (props.viewer.allTodos.count.asInstanceOf[Int] + 1))))
//    )
//
//  }

  override def getConfigs(): Array[Dictionary[Any]] = {
    js.Array(
      js.Dictionary("type" -> "RANGE_ADD", "parentID" -> props.viewer.id, "connectionName" -> "allTodos", "edgeName" -> "changedTodoEdge", "rangeBehaviors" -> js.Dictionary("" -> "prepend")),
      js.Dictionary("type" -> "FIELDS_CHANGE", "fieldIDs" -> js.Dictionary("viewer" -> props.viewer.id))
    )
  }
}
