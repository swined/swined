
const mixin E
{

  public abstract E and(E e)
  public abstract E or(E e)
  public abstract E not()
  
  public E xor(E e) {
    not.and(e).or(and(e.not))
  }
  
}
