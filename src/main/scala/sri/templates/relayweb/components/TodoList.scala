package sri.templates.relayweb.components

import org.scalajs.dom
import sri.core.ElementFactory._
import sri.relay.RelayElementFactory._
import sri.relay.container.RelayContainer.Fragments
import sri.relay.container.{Fragments, RelayContainerSpec}
import sri.relay.query.RelayQL
import sri.relay.{Relay, RelayComponent}

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U, undefined}

object TodoList {


  @ScalaJSDefined
  class Component extends RelayComponent[Props, Unit] {
    def render() = {
      dom.window.console.log(propsDynamic)
      val todosList = getFilteredTodos().map(makeTodo)
      section(json(className = "main"),
        input(json(className = "toggle-all")),
        ul(json(className = "todo-list"), todosList)
      )
    }
    def getFilteredTodos() = {
      val edges = propsDynamic.todos.edges.asInstanceOf[js.Array[js.Dynamic]]
      if (propsDynamic.filter.toString == "active") {
        edges.filter(t => !t.node.complete.asInstanceOf[Boolean])
      } else if (propsDynamic.filter.toString == "completed") {
        edges.filter(t => t.node.complete.asInstanceOf[Boolean])
      } else edges
    }

    def handleToggleAllChange() = {
      val todoCount = propsDynamic.todos.count.asInstanceOf[Int]
      //       val done = propsDynamic.todos.edges
    }

    def makeTodo(edge: js.Dynamic) = {
      Todo(key = edge.node.id.toString, todo = edge.node, viewer = propsDynamic.viewer)
    }

  }

  case class Props()

  val container = Relay.createContainer(getComponentConstructor(js.constructorOf[Component], classOf[Component]), new RelayContainerSpec {
    override val fragments: Fragments = Fragments(
      "todos" -> (() => js.eval(RelayQL(
        """
          fragment on _TodoConnection {
                 count,
                 edges {
                   node {
                     id,
                     complete,
                     ${Todo.getFragment('todo')}
                   }
                 }
               }
        """))),
      "viewer" -> (() => js.eval(RelayQL(
        """
              fragment on ReindexViewer {
                 ${Todo.getFragment('viewer')}
               }
        """)))
    )

  })

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  def apply(todos: js.Dynamic, filter: String, viewer: js.Dynamic, key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createRelayElement(container, json(todos = todos, viewer = viewer, filter = filter))

}
