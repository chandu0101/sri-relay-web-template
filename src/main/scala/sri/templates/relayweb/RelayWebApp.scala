package sri.templates.relayweb

import org.scalajs.dom
import sri.relay.Relay
import sri.relay.container.RelayRootContainer
import sri.relay.network.NetworkLayer
import sri.relay.query.RelayQL
import sri.templates.relayweb.components.{Todo, TodoApp, TodoList}
import sri.templates.relayweb.mutations.{AddTodoMutation, ChangeTodoStatusMutation, ChangeTodoTextMutation, DeleteTodoMutation}
import sri.templates.relayweb.queries.TodoQuery
import sri.web.ReactDOM

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g, literal => json}
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object RelayWebApp extends JSApp {


  @JSExport
  override def main(): Unit = {

    Relay.injectNetworkLayer(new Reindex("https://integrated-branch-17.myreindex.com").getRelayNetworkLayer())

    val addTodoCtor = js.constructorOf[AddTodoMutation]
    g.AddTodoMutation = addTodoCtor
    val addTodoFragFunction: js.Function = () => js.eval(RelayQL(
      """
        fragment on ReindexViewer {
              id
              allTodos {
                count,
              }
             }
      """))
    addTodoCtor.fragments = js.Dictionary("viewer" -> addTodoFragFunction)
    addTodoCtor.getFragment = js.Dynamic.global.Relay.Mutation.getFragment
    val deleteTodoCtor = js.constructorOf[DeleteTodoMutation]
    g.DeleteTodoMutation = deleteTodoCtor
    val deleteFrag: js.Function = () => js.eval(RelayQL(
      """
        fragment on ReindexViewer {
              id
              allTodos(first: 1000000) {
                count,
              }
            }
      """))

    deleteTodoCtor.fragments = js.Dictionary("viewer" -> deleteFrag)
    deleteTodoCtor.getFragment = js.Dynamic.global.Relay.Mutation.getFragment


    g.ChangeTodoStatusMutation = js.constructorOf[ChangeTodoStatusMutation]
    g.ChangeTodoTextMutation = js.constructorOf[ChangeTodoTextMutation]
    g.Todo = Todo.container
    g.TodoList = TodoList.container

    val rc = RelayRootContainer(TodoApp.container, TodoQuery())
    ReactDOM.render(rc, dom.document.getElementById("container"))
  }

}

@js.native
class Reindex(var url: String) extends js.Object {

  def getRelayNetworkLayer(): NetworkLayer = js.native
}