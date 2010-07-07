
const class SourceLocation {
  
  public const Int offset
  public const Int length
  public const Int line
  
  public new make(Str source, Int offset, Int length) {
    this.offset = offset
    this.length = length
    _line := 1
    source.size.times |i| {
      if (source[i] == '\n') _line++
      if (i == offset) line = _line
    }
  }
  
}
