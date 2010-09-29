using build
class Build : build::BuildPod
{
  new make()
  {
    podName = "hr"
    summary = ""
    srcDirs = [`fan/`]
    depends = ["sys 1.0"]
  }
}
