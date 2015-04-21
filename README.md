# Networks-course
В File Transfer лежат клиент и сервер, осуществляющие пересылку файла по протоколу BB8D.
На данный момент готовы следующие методы (у каждого есть два варианта, send и receive):
* Header – handshake. (Не)обязательное приветствие клиента и сервера. Проверяется правильность используемых протокола и версии оного.
* Message – базовая пересылка сообщений в формате UTF-8.
* ActionCode – вызов метода у другого участника диалога. На данный момент функция не протестирована.
* File – передача файла. Сервер должен знать размер принимаемого файла.
* Commit – Подтверждение окончания пересылки данных. В каком-то смысле похоже на нулевой указатель.
