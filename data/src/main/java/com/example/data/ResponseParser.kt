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
        const val PUBIC_CHARACTER_LIST_ERROR = 409

        // Commons errors
        const val UNAUTHORIZED = 401
        const val NOT_FOUND = 404
        const val INTERNAL_ERROR = 500
    }

    inline fun <reified KnownError : Any, reified Success> parseList(
        response: Response<ResponseBody>,
        knownErrorKClasses: Map<Int, KClass<out KnownError>>? = emptyMap()
    ): ParsedResponse<KnownError, List<Success>> = when(response.isSuccessful) {
        true -> parseSuccessList(response)
        false -> when(val either = parseError(response, knownErrorKClasses)) {
            is Either.Left -> either.l
            is Either.Right -> either.r
        }
    }

    inline fun <reified KnownError : Any, reified Success> parse(
        response: Response<ResponseBody>,
        knownErrorKClasses: Map<Int, KClass<out KnownError>>? = emptyMap()
    ): ParsedResponse<KnownError, Success> = try {
        when(response.isSuccessful) {
            true -> parseSuccess(response)
            false -> when(val either = parseError(response, knownErrorKClasses)) {
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
        knownErrorKClassesByErrorCodes: Map<Int, KClass<out KnownError>>?
    ): Either<ParsedResponse.Failure, ParsedResponse.KnownError<KnownError>> {
        val errorBody: String = response.errorBody()!!.string()
        val errorResponse: ErrorResponse =
            jsonParser.fromJson(errorBody, ErrorResponse::class.java)
        val errorCode: String = errorResponse.errors[0].status

        return when (errorCode.toInt()) {
            UNAUTHORIZED -> Either.Left(ParsedResponse.Failure(RepositoryFailure.Unauthorized))
            INTERNAL_ERROR -> Either.Left(ParsedResponse.Failure(RepositoryFailure.ServerError))
            else -> {
                val knownErrorClass: KClass<out KnownError> =
                    knownErrorKClassesByErrorCodes?.get(errorCode) ?: throw Exception()
                val objectInstance: KnownError? = knownErrorClass.objectInstance
                if (objectInstance != null) {
                    return Either.Right(ParsedResponse.KnownError(objectInstance))
                }
                val knownError: KnownError = jsonParser.fromJson(errorBody, knownErrorClass.java)

                Either.Right(ParsedResponse.KnownError(knownError))
            }
        }


    }
}