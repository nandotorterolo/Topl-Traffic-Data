package com.topl.model

import org.rogach.scallop.ScallopConf


class ArgConf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val from = opt[String](required = true, name = "from", descr = "Node from")
  val to = opt[String](required = true, name = "to", descr = "Node to")
  val file = opt[String](required = false, name = "file", descr = "Absolute file path")
  verify()
}
