import com.fasterxml.jackson.annotation.JsonProperty

class DTO(){
    class TeamInfo(
        val name: String,
        val preliminaryRank: Int?,
        val rank: Int ?,
        message: String
    ): Response(message)

    class TeamAndMemberInfo(
        val teamName: String,
        val preliminaryRank: Int?,
        val rank: Int?,
        val memberList: List<MemberRating>,
        message: String
    ): Response(message)

    class MemberInfo(
        val teamName: String,
        val id: String
    )

    class MemberRating(
        val id: String,
        val rating: Int
    )
    open class Response(
        val message: String
    )

    class Result(
        val contestId: String,
        val contestName: String,
        val handle: String,
        val rank: Int,
        val ratingUpdateTimeSeconds: Int,
        val oldRating: Int,
        val newRating: Int
    )
    class UserRating(
        val status: String,
        val result: List<Result>
    )
}