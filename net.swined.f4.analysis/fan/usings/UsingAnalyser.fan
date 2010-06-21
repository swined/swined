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
      fpod.typeRefs.table.each |FTypeRef r| {
        usings.findAll |Using u -> Bool| { 
          matches(fpod, r, u)
        }.each { usings.remove(it) }
      }
      allUsings.addAll(usings)
    }
    return allUsings.unique
  }
  
  private Bool matches(FPod fpod, FTypeRef ref, Using u) {
    if (fpod.n(ref.podName) != u.podName) return false
    if (u.resolvedType != null && fpod.n(ref.typeName) != u.resolvedType.name) return false
    return true
  }
  
}