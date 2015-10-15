package sri.templates.relayweb.components

import org.scalajs.dom
import sri.core.ElementFactory._
import sri.relay.RelayElementFactory._
import sri.relay.container.RelayContainer.Fragments
import sri.relay.container.RelayContainer.Fragments
import sri.relay.container.{Fragments, RelayContainerSpec}
import sri.relay.query.RelayQL
import sri.relay.{RelayElementFactory, Relay, RelayComponent}
import sri.templates.relayweb.mutations.{AddTodoMutation, DeleteTodoMutation}

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U}


object TodoApp {

  case class State(selectedFilter: String = "all")

  @ScalaJSDefined
  class Component extends RelayComponent[Props, State] {

    initialState(State())

    def render() = {
      dom.window.console.log(propsDynamic)
      section(json(className = "todoapp"),
        makeHeader(),
        TodoList(todos = propsDynamic.viewer.allTodos, filter = state.selectedFilter, viewer = propsDynamic.viewer),
        makeFooter()
      )
    }

    def handleFilterChange(filter: String) = {
      setState(state.copy(selectedFilter = filter))
    }

    def handleInputSave(text: String) = {
      println(s"trying to add text : $text")
      Relay.Store.update(new AddTodoMutation(json(text = text, viewer = propsDynamic.viewer)))
    }

    def handleClearCompleted() = {
      val edges = propsDynamic.viewer.allTodos.edges.asInstanceOf[js.Array[js.Dynamic]]
      edges.filter(e => e.node.complete.asInstanceOf[Boolean])
        .foreach(edge => {
        Relay.Store.update(new DeleteTodoMutation(json(id = edge.node.id, viewer = propsDynamic.viewer)))
      })
    }

    def makeHeader() = {
      header(json(className = "header"),
        h1(null, "Todos"),
        TodoInput(className = "new-todo", onSave = handleInputSave _)
      )
    }

    def makeFooter() = {
      div(json(className = "footer"), "footer")
    }

  }

  case class Props()

  val tctor = getComponentConstructor(js.constructorOf[Component],classOf[Component])
  val container = Relay.createContainer(tctor,new RelayContainerSpec {
    override val fragments: Fragments = Fragments("viewer" -> (() => js.eval(RelayQL(
      """
        fragment on ReindexViewer {
                allTodos(first: 1000000) {
                  count,
                  edges {
                    node {
                      id,
                      complete
                    }
                  }
                  ${TodoList.getFragment('todos')}
                },
                ${TodoList.getFragment('viewer')}
                ${AddTodoMutation.getFragment('viewer')}
                ${DeleteTodoMutation.getFragment('viewer')}
              }
      """))))

  })

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  def apply(key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElement(ctor, null, key = key, ref = ref)

}
