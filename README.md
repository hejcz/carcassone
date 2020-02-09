## General information
Implementation of Carcassonne board game logic in kotlin.
Right now the game supports:
- Core game
- Inns and cathedrals extension
- River extension
- Abbot extension
- Corn circles extension

It validates moves, rewards players regarding rules introduced in core and extensions and informs players what to do next.

## Example setup
```kotlin
fun main() {
    val game = Game(
        listOf(Player(id = 10289, order = 1), Player(id = 776, order = 2), Player(id = 12, order = 3)),
        GameSetup(AbbotExtension, InnAndCathedralsExtension, RiverExtension)
    )
    .dispatch(Begin) // returns PlaceTile event with name of drawn tile
    .dispatch(PutTile(Position(0, 1), NoRotation)) // returns PutPiece event
    // you can either skip piece
    .dispatch(SkipPiece)
    // or put it
    .dispatch(PutPiece(SmallPiece, Knight(Down)))
    // next player makes his/her move
}
```
