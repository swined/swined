using f4parser

class DynamicAnalyser : AstVisitor {
  
  public Node[] nodes := [,] { private set }
  
  override Bool enterNode(Node n) {
    i := n as InvokeExpr
    if (i == null) return true
    if (i.id != ExprId.dynamicInvoke) return true
    if (i.caller is SlotRef) nodes.add(n)
    return true
  }
  
}
