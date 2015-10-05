package sri.templates.relayweb.mutations

import sri.relay.mutation.RelayMutation
import sri.relay.query.RelayQL
import sri.relay.tools.RelayTypes.MutationFragment

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{Any, Array, Dictionary, UndefOr}

@ScalaJSDefined
class ChangeTodoTextMutation(props : js.Dynamic) extends RelayMutation(props) {
  override def getMutation(): MutationFragment = js.eval(RelayQL( """mutation {updateTodo}"""))

  override def getVariables(): js.Object = json(id = props.id, text = props.text)

  override def getFatQuery(): Any = js.eval(RelayQL(
    """
       fragment on _TodoPayload {
              changedTodo {
                text,
              },
            }
    """))

  override def getConfigs(): Array[Dictionary[Any]] = js.Array(
    js.Dictionary("type" -> "FIELDS_CHANGE", "fieldIDs" -> js.Dictionary("changedTodo" -> props.id))
  )

//  override def getOptimisticResponse(): UndefOr[js.Object] = {
//    json(changedTodo = json(id = props.id, text = props.text))
//  }
}
