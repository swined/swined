
const mixin E
{

  protected abstract E andImpl(E e)
  protected abstract E orImpl(E e)
  protected abstract E notImpl()
  protected abstract E optImpl()
  protected abstract E subImpl(V v)
  
  public E xor(E e) {
    not.and(e).or(and(e.not)).optImpl
  }

  public E and(E e) {
    andImpl(e).optImpl
  }
  
  public E or(E e) {
    orImpl(e).optImpl
  }
  
  public E not() {
    notImpl.optImpl
  }
  
}
