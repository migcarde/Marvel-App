package com.example.data

import com.example.commons.data.types.Either
import com.example.commons.json.JsonParser
import com.example.domain.RepositoryFailure
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.reflect.KClass

class ResponseParser(val jsonParser: JsonParser) {

    companion object {

        // Custom errors
        const val PUBIC_CHARACTER_LIST_ERROR = "409"

        // Commons errors
        const val UNAUTHORIZED = 401
        const val NOT_FOUND = 404
        const val INTERNAL_ERROR = 500
    }

    inline fun <reified KnownError : Any, reified Success> parseList(
        response: Response<ResponseBody>,
        knownErrorKClasses: Map<String, KClass<out KnownError>>? = emptyMap()
    ): ParsedResponse<KnownError, List<Success>> = when (response.isSuccessful) {
        true -> parseSuccessList(response)
        false -> when (val either = parseError(response, knownErrorKClasses)) {
            is Either.Left -> either.l
            is Either.Right -> either.r
        }
    }

    inline fun <reified KnownError : Any, reified Success> parse(
        response: Response<ResponseBody>,
        knownErrorKClasses: Map<String, KClass<out KnownError>>? = emptyMap()
    ): ParsedResponse<KnownError, Success> = try {

        if (response.isSuccessful) {
            parseSuccess(response)
        } else {
            when (val either = parseError(response, knownErrorKClasses)) {
                is Either.Left -> either.l
                is Either.Right -> either.r
            }
        }
    } catch (e: Exception) {
        throw e
    }

    inline fun <reified Success> parseSuccess(response: Response<ResponseBody>): ParsedResponse.Success<Success> {
        val successBody: String = response.body()!!.string()
        val success = jsonParser.fromJson(successBody, Success::class.java)

        return ParsedResponse.Success(success)
    }

    inline fun <reified Success> parseSuccessList(response: Response<ResponseBody>): ParsedResponse.Success<List<Success>> {
        val successBody: String = response.body()!!.string()
        val success = jsonParser.fromJsonList(successBody, Success::class.java)

        return ParsedResponse.Success(success)
    }

    inline fun <reified KnownError : Any> parseError(
        response: Response<ResponseBody>,
        knownErrorKClassesByErrorCodes: Map<String, KClass<out KnownError>>?
    ): Either<ParsedResponse.Failure, ParsedResponse.KnownError<KnownError>> {
        val errorBody: String = response.errorBody()!!.string()
        val errorResponse: ErrorResponse =
            jsonParser.fromJson(errorBody, ErrorResponse::class.java)

        if (errorResponse.code == UNAUTHORIZED) {
            return Either.Left(ParsedResponse.Failure(RepositoryFailure.Unauthorized))
        } else if (errorResponse.code == INTERNAL_ERROR) {
            return Either.Left(ParsedResponse.Failure(RepositoryFailure.ServerError))
        }

        val knownErrorClass: KClass<out KnownError> =
            knownErrorKClassesByErrorCodes?.get(errorResponse.code.toString()) ?: throw Exception()
        val knownError: KnownError = jsonParser.fromJson(errorBody, knownErrorClass.java)
        return Either.Right(ParsedResponse.KnownError(knownError))

    }
}