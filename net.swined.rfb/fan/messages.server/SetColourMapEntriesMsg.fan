
const class SetColourMapEntriesMsg : IServerMsg
{
  
  override const Int messageType := 1
  public const Int firstColour
  public const ColourMapEntry[] entries
  
  public new read(InStream in) {
    checkMessageType(in, 1)
    firstColour = in.readU2
    numColours := in.readU2
    entries := [,]
    numColours.times { entries.add(ColourMapEntry(in)) }
    this.entries = entries
  }
  
}
