resolvers += Resolver.url(
  "sbt-plugin-releases",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

resolvers += "scct-github-repository" at "http://mtkopone.github.com/scct/maven-repo"


addSbtPlugin("com.github.retronym" % "sbt-onejar" % "0.8")

addSbtPlugin("reaktor" %% "sbt-scct" % "0.2-SNAPSHOT")
