package sri.templates.relayweb.components

import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html
import org.scalajs.dom.raw.KeyboardEvent
import sri.core.ElementFactory._
import sri.core.ReactComponent
import sri.web.ReactDOM

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U}


object TodoInput {

  case class State(text: String = "")

  @ScalaJSDefined
  class Component extends ReactComponent[Props, State] {

    initialState(State())


    override def componentWillMount(): Unit = {
      setState(state.copy(text = props.initialValue))
    }

    def render() = input(json(className = props.className,
      value = state.text,
      onChange = handleChange _,
      onKeyDown = handleKeyDown _))


    override def componentDidMount(): Unit = {
      ReactDOM.findDOMNode(this).asInstanceOf[html.Input].focus()
    }

    def handleChange(e: js.Dynamic) = {
      setState(state.copy(text = e.target.value.toString))
    }

    def handleKeyDown(e: KeyboardEvent) = {
      if (e.keyCode == KeyCode.Escape) {
        if (props.onCancel != null) props.onCancel()
      } else if (e.keyCode == KeyCode.Enter) save()
    }

    def save() = {
      val text = state.text.trim
      println(s"saving text $text")
      if (text.isEmpty) {
        if (props.onDelete != null) props.onDelete()
      } else if (text == props.initialValue) {
        if (props.onCancel != null) props.onCancel()
      } else {
        if (props.onSave != null) props.onSave(text)
        setState(state.copy(text = ""))
      }
    }
  }

  case class Props(initialValue: String = "", className: String, onSave: (String) => _, onDelete: () => _, onCancel: () => _)

  val factory = getComponentFactory(js.constructorOf[Component], classOf[Component])

  def apply(initialValue: String = "", className: String = "", onSave: (String) => _, onDelete: () => _ = null, onCancel: () => _ = null, key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElement(factory, Props(initialValue, className, onSave, onDelete, onCancel), key = key, ref = ref)

}
