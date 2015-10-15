package sri.templates.relayweb.components

import org.scalajs.dom
import sri.core.ElementFactory._
import sri.core.ReactElement
import sri.relay.RelayElementFactory._
import sri.relay.container.RelayContainer.Fragments
import sri.relay.container.{Fragments, RelayContainerSpec}
import sri.relay.query.RelayQL
import sri.relay.{Relay, RelayComponent}
import sri.templates.relayweb.mutations.{ChangeTodoStatusMutation, ChangeTodoTextMutation, DeleteTodoMutation}

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U}


object Todo {

  case class State(isEditing: Boolean = false)

  @ScalaJSDefined
  class Component extends RelayComponent[Props, State] {
    initialState(State())

    def render() = {
      var className = ""
      if (propsDynamic.todo.complete.asInstanceOf[Boolean]) className += " completed"
      if (state.isEditing) className += " editing"
      li(json(className = className),
        div(json(className = "view"),
          input(json(
            checked = propsDynamic.todo.complete.asInstanceOf[Boolean],
            className = "toggle",
            `type` = "checkbox",
            onChange = handleCompleteChange _)),
          label(json(onDoubleClick = handleLabelDoubleClick _), propsDynamic.todo.text.toString),
          button(json(className = "destroy", onClick = handleDestroyClick _))
        ),
        makeInput()
      )

    }

    def handleCompleteChange() = {
      println(s"complete clicked")
      Relay.Store.update(new ChangeTodoStatusMutation(json(id = propsDynamic.todo.id, complete = !propsDynamic.todo.complete.asInstanceOf[Boolean])))
    }

    def handleLabelDoubleClick() = {
      println(s"handling double click")
      setState(state.copy(isEditing = true))
    }

    def handleDestroyClick() = {
      println(s"deleting yay")
      dom.window.console.log(propsDynamic)
      Relay.Store.update(new DeleteTodoMutation(json(id = propsDynamic.todo.id, viewer = propsDynamic.viewer)))
    }

    def handleInputSave(text: String) = {
      Relay.Store.update(new ChangeTodoTextMutation(json(id = propsDynamic.todo.id, text = text)))
      setState(state.copy(isEditing = false))
    }

    def handleInputCancel() = {
      setState(state.copy(isEditing = false))
    }

    def handleInputDelete() = {
      setState(state.copy(isEditing = false))
    }

    def makeInput(): ReactElement = {
      if (state.isEditing) TodoInput(className = "edit", initialValue = propsDynamic.todo.text.toString, onSave = handleInputSave _, onCancel = handleInputCancel _, onDelete = handleInputDelete _) else null
    }

  }

  case class Props()

  val container = Relay.createContainer(getComponentConstructor(js.constructorOf[Component], classOf[Component]), new RelayContainerSpec {
    override val fragments: Fragments = Fragments("todo" -> (() => js.eval(RelayQL(
      """
            fragment on Todo {
                id,
                text,
                complete
              }
      """))),
      "viewer" -> (() => js.eval(RelayQL(
        """
            fragment on ReindexViewer {
                ${DeleteTodoMutation.getFragment('viewer')}
              }
        """)))
    )

  })

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  def apply(todo: js.Dynamic, viewer: js.Dynamic, key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createRelayElement(container, json(todo = todo, viewer = viewer), key = key)

}
