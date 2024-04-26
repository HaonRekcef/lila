package views.puzzle

import lila.app.templating.Environment.{ *, given }

import scalalib.paginator.Paginator
import lila.puzzle.Puzzle

object ofPlayer:

  def apply(query: String, user: Option[User], puzzles: Option[Paginator[Puzzle]])(using ctx: PageContext) =
    views.base.layout(
      title = user.fold(trans.puzzle.lookupOfPlayer.txt())(u => trans.puzzle.fromXGames.txt(u.username)),
      moreCss = cssTag("puzzle.page"),
      modules = infiniteScrollTag
    )(
      main(cls := "page-menu")(
        bits.pageMenu("player", user),
        div(cls := "page-menu__content puzzle-of-player box box-pad")(
          form(
            action := routes.Puzzle.ofPlayer(),
            method := "get",
            cls    := "form3 puzzle-of-player__form complete-parent"
          )(
            st.input(
              name         := "name",
              value        := query,
              cls          := "form-control user-autocomplete",
              placeholder  := trans.clas.lichessUsername.txt(),
              autocomplete := "off",
              dataTag      := "span",
              autofocus
            ),
            submitButton(cls := "button")(trans.puzzle.searchPuzzles.txt())
          ),
          div(cls := "puzzle-of-player__results")(
            (user, puzzles) match
              case (Some(u), Some(pager)) =>
                if pager.nbResults == 0 && ctx.is(u) then p(trans.puzzle.fromMyGamesNone())
                else
                  frag(
                    p(strong(trans.puzzle.fromXGamesFound((pager.nbResults), userLink(u)))),
                    div(cls := "puzzle-of-player__pager infinite-scroll")(
                      pager.currentPageResults.map { puzzle =>
                        div(cls := "puzzle-of-player__puzzle")(
                          chessgroundMini(
                            fen = puzzle.fenAfterInitialMove.board,
                            color = puzzle.color,
                            lastMove = puzzle.line.head.some
                          )(
                            a(
                              cls  := s"puzzle-of-player__puzzle__board",
                              href := routes.Puzzle.show(puzzle.id)
                            )
                          ),
                          span(cls := "puzzle-of-player__puzzle__meta")(
                            span(cls := "puzzle-of-player__puzzle__id", s"#${puzzle.id}"),
                            span(cls := "puzzle-of-player__puzzle__rating", puzzle.glicko.intRating)
                          )
                        )
                      },
                      pagerNext(pager, np => s"${routes.Puzzle.ofPlayer(u.username.some, np).url}")
                    )
                  )
              case (_, _) => emptyFrag
          )
        )
      )
    )
