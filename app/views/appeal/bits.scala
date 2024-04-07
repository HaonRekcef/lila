package views.html
package appeal

import lila.app.templating.Environment.{ *, given }
import lila.app.ui.ScalatagsTemplate.*

object bits:

  def layout(title: String)(body: Frag)(using PageContext) =
    views.html.base.layout(
      title = title,
      moreCss = frag(
        cssTag("form3"),
        cssTag("appeal"),
        isGranted(_.UserModView).option(cssTag("mod.user"))
      ),
      modules = jsModule("mod.appeal") :: isGranted(_.UserModView).thenList(jsModule("mod.user"))
    )(body)
