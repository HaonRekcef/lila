package views.appeal

import lila.app.templating.Environment.{ *, given }

object bits:

  def layout(title: String)(body: Frag)(using PageContext) =
    views.base.layout(
      title = title,
      moreCss = frag(
        cssTag("form3"),
        cssTag("appeal"),
        isGranted(_.UserModView).option(cssTag("mod.user"))
      ),
      modules = jsModule("bits.appeal") ++ isGranted(_.UserModView).so(jsModule("mod.user"))
    )(body)
