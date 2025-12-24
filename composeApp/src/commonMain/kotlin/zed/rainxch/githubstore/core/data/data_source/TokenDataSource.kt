package zed.rainxch.githubstore.core.data.data_source

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import zed.rainxch.githubstore.core.domain.model.ApiPlatform
import zed.rainxch.githubstore.core.domain.model.DeviceTokenSuccess
import zed.rainxch.githubstore.feature.auth.data.TokenStore
import kotlin.concurrent.atomics.ExperimentalAtomicApi

interface TokenDataSource {
    fun tokenFlow(apiPlatform: ApiPlatform): StateFlow<DeviceTokenSuccess?>
    suspend fun save(apiPlatform: ApiPlatform, token: DeviceTokenSuccess)
    suspend fun reloadFromStore(apiPlatform: ApiPlatform): DeviceTokenSuccess?
    suspend fun clear(apiPlatform: ApiPlatform)

    fun current(): DeviceTokenSuccess?
}

@OptIn(ExperimentalAtomicApi::class)
class DefaultTokenDataSource(
    private val tokenStore: TokenStore,
    private val apiPlatform: ApiPlatform,
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : TokenDataSource {
    private val _flow = MutableStateFlow<DeviceTokenSuccess?>(null)

    private val isInitialized = CompletableDeferred<Unit>()

    override fun tokenFlow(apiPlatform: ApiPlatform): StateFlow<DeviceTokenSuccess?> {
        return _flow
    }

    init {
        scope.launch {
            try {
                val token = tokenStore.load(apiPlatform)
                _flow.value = token
            } finally {
                isInitialized.complete(Unit)
            }
        }
    }

    override suspend fun save(apiPlatform: ApiPlatform, token: DeviceTokenSuccess) {
        tokenStore.save(
            apiPlatform = apiPlatform,
            token = token
        )
        _flow.value = token
    }

    override suspend fun reloadFromStore(apiPlatform: ApiPlatform): DeviceTokenSuccess? {
        isInitialized.await()
        return _flow.value
    }

    override suspend fun clear(apiPlatform: ApiPlatform) {
        tokenStore.clear(
            apiPlatform = apiPlatform
        )

        _flow.value = null
    }

    override fun current(): DeviceTokenSuccess? = _flow.value
}
