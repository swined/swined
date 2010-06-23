using f4core
using f4builder
using compiler

const class UsingAnalyser
{
  
  private const FantomProject project
  
  public new make(FantomProject project) {
    this.project = project
  }
  
  private CompilerInput initInput() {
    buf := StrBuf()
    input := CompilerInput.make
    input.log         = CompilerLog(buf.out)
    input.podName     = project.podName
    input.version     = project.version
    input.ns          = F4Namespace(project.depends, project.classpath)
    input.depends     = project.rawDepends.dup
    input.includeDoc  = true
    input.summary     = project.summary
    input.mode        = CompilerInputMode.file
    input.baseDir     = project.baseDir
    input.srcDirs     = project.srcDirs
    input.resDirs     = project.resDirs
    input.index       = project.index
    input.output      = CompilerOutputMode.transientPod
    return input
  }
  
  public Using[] analyse() {
    c := Compiler(initInput)
    c.frontend
    allUsings := [,]
    c.pod.units.each {
      c.types = it.types
      fpod := Assembler(c).assemblePod
      usings := it.usings.dup
      used := [,]
      fpod.typeRefs.table.each |FTypeRef r| {
        rm := (usings.map |Using u -> Match[]| {
          m := matches(fpod, r, u)
          return m > 0 ? [Match(m, u)] : [,]
        }.flatten.sort.last as Match)?.usingDef
        if (rm != null) used.add(rm)
      }
      used.each { usings.remove(it) }
      allUsings.addAll(usings)
    }
    return allUsings.unique
  }
  
  private Int matches(FPod fpod, FTypeRef ref, Using u) {
    if (fpod.n(ref.podName) != u.podName) return 0
    if (u.typeName == null) {
      return 2
    } else {
      if (u.asName == null) {
        return fpod.n(ref.typeName) == u.typeName ? 1 : 0
      } else {
        return fpod.n(ref.typeName) == u.typeName ? 3 : 0
      }
    }
  }
  
}

class Match {
  
  public const Int rank 
  public readonly Using usingDef
  
  public new make(Int rank, Using usingDef) {
    this.rank = rank
    this.usingDef = usingDef
  }
  
  override Int compare(Obj o) {
    return rank <=> (o as Match)?.rank
  }
  
}