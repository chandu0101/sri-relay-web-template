
 // START OF QUERY

Relay.QL`
            fragment on ReindexViewer {
                ${DeleteTodoMutation.getFragment('viewer')}
              }
        `

 // END OF QUERY

       