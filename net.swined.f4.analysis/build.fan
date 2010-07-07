using build
class Build : build::BuildPod
{
  new make()
  {
    podName = "f4analysis"
    summary = ""
    srcDirs = [`fan/`, `fan/usings/`, `fan/dynamic/`]
    outDir = `./`
    depends = ["sys 1.0", "compiler 1.0", "f4core 1.0", "f4builder 1.0", "f4parser 1.0", "f4model 1.0"]
  }
}
