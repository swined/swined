
const mixin E
{

  protected abstract E andImpl(E e)
  protected abstract E orImpl(E e)
  protected abstract E notImpl()
  
  public E xor(E e) {
    not.and(e).or(and(e.not))
  }

  public E and(E e) {
    andImpl(e)
  }
  
  public E or(E e) {
    orImpl(e)
  }
  
  public E not() {
    notImpl
  }
  
}
