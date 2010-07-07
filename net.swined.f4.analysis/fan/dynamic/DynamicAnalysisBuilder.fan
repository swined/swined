using f4core
using f4builder
using f4parser
using [java]java.util::List as JList 
using [java]java.util::Set as JSet
using [java]org.eclipse.core.runtime
using [java]org.eclipse.core.resources::IFile
using [java]org.eclipse.core.resources::IResource
using [java]org.eclipse.core.resources::IProject
using [java]org.eclipse.core.resources::ResourcesPlugin
using [java]org.eclipse.dltk.compiler.problem::DefaultProblem
using [java]org.eclipse.dltk.compiler.problem::ProblemSeverities
using [java]org.eclipse.dltk.core
using [java]org.eclipse.dltk.core.builder
using [java]org.eclipse.dltk.core.builder::IScriptBuilder$DependencyResponse as DependencyResponse
using [java]org.eclipse.core.filesystem::URIUtil

class DynamicAnalysisBuilder : IScriptBuilder
{
  override Void initialize(IScriptProject? project)
  {
  }

  override IStatus? buildResources(IScriptProject? project, JList? resList,
      IProgressMonitor? monitor, Int status)
  {
    return Status(IStatus.OK, pluginId, "OK")
  }
  
  private Void scan(FantomProject fp, IModelElement[] model) {
    if(fp.hasErrs)
      return
    model.each |m| {
      unit := Parser(m->getSourceContents, fp.ns).cunit
      a := DynamicAnalyser()
      unit.accept(a) 
      a.nodes.each |n| { 
        l := SourceLocation(m->getSourceContents, n.start, n.end)
        report(m.getUnderlyingResource, l, fp.project)
      }
    }
  }

  private IResource resource(IProject project, Str? file)
  {
    if (file != null) 
    {
      IFile[] files := ResourcesPlugin.getWorkspace.getRoot.
        findFilesForLocationURI(URIUtil.toURI(Path(file).makeAbsolute))
      if (!files.isEmpty) return files.first
    }
    return project
  }
  
  private Void report(IResource resource, SourceLocation l, IProject p) {
    reporter := reporters.getOrAdd(resource.getLocationURI.toString) |->Obj| { ProblemReporter(resource) }
    reporter.reportProblem(
      DefaultProblem(
        resource.getLocation.toString, 
        "Redundant dynamic call", 
        0, //id (don't know what this means)
        Str[,], //arguments (don't know what this means)
        ProblemSeverities.Warning, //severity
        -1, //start position
        -1, //end position
        l.line,
        -1
        )
      )
  }
  
  override DependencyResponse? getDependencies(IScriptProject? project, Int buildType,
      JSet? localElements, JSet? externalElements, JSet? oldExternalFolders,
      JSet? externalFolders)
  {
    return null
  }

  override Void clean(IScriptProject? project, IProgressMonitor? monitor)
  {
  } 

  override Void endBuild(IScriptProject? project, IProgressMonitor? monitor) 
  {
  }
  
  static const Str pluginId := "net.swined.f4.analyser.dynamic"

  override IStatus? buildModelElements(IScriptProject? project, JList? elements,
      IProgressMonitor? monitor, Int status)
  {
    scan(fantomProject(project), elements.toArray)
    reporters.vals.each { it.flush }
    reporters.clear
    return Status(IStatus.OK, pluginId, "OK")
  }
  
  private FantomProject fantomProject(IScriptProject project)
  {
    FantomProjectManager.instance[project.getProject]
  }

  private Str:ProblemReporter reporters := [Str:ProblemReporter][:]
  
}

