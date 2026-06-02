# Yapılacaklar

## capacity_exceeded hata yönetimi
`EventDetailViewModel`'de `createPurchase` onFailure bloğuna eklenecek:
- `id`'yi `private val eventId: String` olarak sakla
- `capacity_exceeded` gelince `loadEvent(eventId)` çağır
- `ApiException.errorMessage` alanını kontrol et

## toUserMessage — ortak yere taşı
Şu an `LoginViewModel.kt` içinde `internal` tanımlı.
`core/util/ErrorMessages.kt` dosyasına taşı, 403/409 kodlarını genişlet.
Tüm ViewModel'lar buradan import etsin.
