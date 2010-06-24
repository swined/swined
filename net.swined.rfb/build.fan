using build
class Build : build::BuildPod
{
  new make()
  {
    podName = "rfb"
    summary = ""
    srcDirs = [`fan/`, `fan/struct/`, `fan/messages.server/`, `fan/messages.client/`]
    depends = ["sys 1.0", "inet 1.0"]
  }
}
