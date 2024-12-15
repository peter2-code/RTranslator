<img src="https://github.com/niedev/RTranslator/blob/v2.00/images/logo_beta_cut.png" width="280">

RTranslator是一个（几乎）开源，免费和离线的Android实时翻译应用程序。

连接到拥有该应用程序的人，连接蓝牙耳机，把手机放在口袋里，你就可以像对方说你的语言一样进行对话。
<br /><br />

![Conversation mode](https://github.com/niedev/RTranslator/blob/v2.00/images/Conversation_image.png)
<br /><br />
![WalkieTalkie mode and Costs](https://github.com/niedev/RTranslator/blob/v2.00/images/TextTranslation_and_WalkieTalkie.png)
<br /><br />

<h3>Conversation mode 对话模式</h3>

对话模式是RTranslator的主要功能。在此模式下，您可以与使用此应用程序的另一部手机连接。如果用户接受您的连接请求：


- 当您通话时，您的手机（或蓝牙耳机，如果已连接）将捕获音频。

- 捕获的音频将被转换为文本并发送到对话者的手机。

- 对话者的手机将把收到的文本翻译成他的语言。

- 对话者的电话将翻译的文本转换成音频，并将从其扬声器（或通过对话者的蓝牙耳机，如果连接到他的电话）再现它。

所有这一切都是双向的。

每个用户可以有多个连接的电话，这样你就可以翻译两个以上的人之间的对话，并在任何组合。
<br /><br />

<h3>WalkieTalkie mode对讲机模式</h3>

如果对话模式对于与某人进行长时间对话很有用，则此模式是为快速对话而设计的，例如在街上询问信息或与店员交谈。

该模式仅翻译两个人之间的对话，不适用于蓝牙耳机，并且必须轮流交谈。这不是一个真实的同声传译，但它可以工作，只有一个电话。

在此模式下，智能手机麦克风将同时收听两种语言（可在对讲机模式的同一屏幕中选择）。 <br />
该应用程序将检测对话者正在使用哪种语言，将音频翻译成另一种语言，将文本转换为音频，然后从手机扬声器中再现。当TTS结束时，它将自动恢复收听。
<br /><br />

<h3>文本翻译模式</h3>

这个模式只是一个经典的文本翻译器，但总是有用的。
<br /><br />

<h3>General</h3>

RTranslator使用 <a href="https://ai.meta.com/research/no-language-left-behind/">Meta\'s NLLB</a> 进行翻译，使用 <a href="https://openai.com/index/whisper/">OpenAi\'s Whisper</a> 进行语音识别,两者都是（几乎）开源和最先进的AI，具有出色的质量，直接在手机上运行，确保绝对的隐私，甚至离线使用RTranslator的可能性，而不会损失质量。

此外，RTranslator甚至可以在后台工作，手机处于待机状态或使用其他应用程序时（仅当您使用对话或对讲机模式时）。然而，有些手机会限制后台的电源，所以在这种情况下，最好避免它，并在屏幕上保持应用程序打开。
<br />

<a href="https://www.producthunt.com/posts/rtranslator-2?embed=true&utm_source=badge-featured&utm_medium=badge&utm_souce=badge-rtranslator&#0045;2" target="_blank"><img src="https://api.producthunt.com/widgets/embed-image/v1/featured.svg?post_id=487672&theme=light" alt="RTranslator - Open&#0045;source&#0032;and&#0032;offline&#0032;simultaneous&#0032;translator&#0032;for&#0032;Android | Product Hunt" style="width: 250px; height: 54px;" width="250" height="54" /></a>
<br /><br />

<h3>2.1版的新功能</h3>

- 新GUI！由Chiara Chindamo设计。 [Chiara Chindamo](https://www.linkedin.com/in/chiara-chindamo-946053234/).

- 在文本翻译模式中增加了朗读和复制按钮。

- 在WalkieTalkie模式下增加了手动控制键盘的选项。

- 添加了使用低质量语言的选项。

- 修复了一些bug。

For the full list of changes see [here](https://github.com/niedev/RTranslator/releases/tag/2.1.0).
<br /><br />

<h3>性能要求</h3>

我已经优化了AI模型，以最大限度地减少RAM消耗和执行时间，尽管如此，为了能够在没有崩溃风险的情况下使用应用程序，您需要至少6GB RAM的手机，并且要有足够好的执行时间，您需要具有足够快的CPU的手机。

如果你有一个相当蹩脚的手机（或者如果你想要最大的速度），你总是可以使用 <a href="https://github.com/niedev/RTranslator/tree/v1.00">1.0版的RTranslator</a> （但由于它使用谷歌API，它不是免费的，需要一些初始设置）。
<br /><br />


<h3>下载</h3>

要安装应用程序，请从https://github.com/niedev/RTranslator/releases/下载最新版本的应用程序apk文件并安装它（忽略其他文件，这些文件将在第一次启动时由应用程序自动下载）。

在第一次启动时，RTranslator将自动下载翻译和语音识别模型（1.2GB），一旦完成，您就可以开始翻译。

最初的下载将从GitHub获得模型，但是在某些地区GitHub非常慢，那些有这种问题的人可以从计算机单独下载模型（或者通常以他们喜欢的任何方式）并按照 <a href="https://github.com/niedev/RTranslator/blob/v2.00/Sideloading.md">本指南</a> 手动将它们插入应用程序。 

If you have a GitHub account and want to be notified when a new release comes out you can do so by clicking, at the top of the page, on "Watch" -> "Custom" -> "Releases" -> "Apply".
<br /><br />


<h3>Supported languages</h3>

The languages supported are as follows:

Arabic, Bulgarian, Catalan, Chinese, Croatian, Czech, Danish, Dutch, English, Finnish, French, Galician,  German, Greek, Italian, Japanese, Korean, Macedonian, Polish, Portuguese, Romanian, Russian, Slovak, Spanish, Swedish, Tamil, Thai, Turkish, Ukrainian, Urdu, Vietnamese.
<br /><br />
If your language is not on the list, from version 2.1 of RTranslator you can go into the settings and enable "**Support low quality languages**" to add the following languages (which have lower quality for translation and speech recognition):

Afrikaans, Akan (only text), Amharic, Assamese, Bambara (only text), Bangla, Bashkir, Basque, Belarusian, Bosnian, Dzongkha (only text), Esperanto (only text), Estonian, Ewe (only text), Faroese, Fijian (only text), Georgian, Guarani (only text), Gujarati, Hausa, Hebrew, Hindi, Hungarian, Irish (only text), Javanese (only text), Kannada, Kashmiri (only text), Kazakh, Kikuyu (only text), Kinyarwanda (only text), Korean, Kyrgyz (only text), Lao, Limburghish (only text), Lingala, Lithuanian, Luxembourghish, Macedonian, Tagalog (only text), Tibetan.
<br /><br />


<h3>Text To Speech</h3>

To speak, RTranslator uses the system TTS of your phone, so the quality of the latter and the supported languages ​​depend on the system TTS of your phone.

The supported languages ​​seen above are all compatible with <a href="https://play.google.com/store/apps/details?id=com.google.android.tts&pcampaignid=web_share">Google TTS</a>, which is the recommended TTS (although you can use the TTS you want).

To change the system TTS (and therefore the TTS used by RTranslator), download the TTS you want to use from the Play Store, or from the source you prefer, and open RTranslator, then open its settings (top right) and, in the "Output" section, click on "Text to Speech", at this point the system settings will open in the section where you can select the preferred system TTS engine (among those installed), at this point, if you have changed the preferred engine, restart RTranslator to apply the changes.
<br /><br />

<h3>Privacy</h3>

Privacy is a fundamental right. That's why RTranslator does not collect any personal data (I don't even have a server). For more information, read the <a href="https://github.com/niedev/RTranslator/blob/v2.00/privacy/Privacy_Policy_en.md" target="_blank" rel="noopener noreferrer">privacy policy</a> (for now is the same privacy policy of RTranslator 1.0, but I will update it in the future).
<br /><br />

<h3>Libraries and models</h3>

RTranslator code is completely open-source, but some of the external libraries it uses have less permissive licenses, these are all the external libraries used by the app (with the indication of their license):

<a href="https://github.com/niedev/BluetoothCommunicator">BluetoothCommunicator</a> (open-source): Used for Bluetooth LE communication between devices.

<a href="https://github.com/niedev/GalleryImageSelector">GalleryImageSelector</a> (open-source): Used for selecting and cropping the profile image from the gallery.

[OnnxRuntime](https://github.com/microsoft/onnxruntime) (open-source): Used as an accelerator engine for the AI models.

<a href="https://github.com/google/sentencepiece">SentencePiece</a> (open-source): Used for tokenization of the input text for NLLB.

<a href="https://developers.google.com/ml-kit/language/identification">Ml Kit</a> (closed-source): Used for the identification of the language in the WalkieTalkie mode.
<br /><br />
And the following AI models:

<a href="https://github.com/facebookresearch/fairseq/tree/nllb">NLLB</a> (open-source, but only for non-commercial use): The model used is NLLB-Distilled-600M with KV cache.

<a href="https://github.com/openai/whisper">Whisper</a> (open-source): The model used is Whisper-Small-244M with KV cache.
<br /><br />

<h3>Performance of the models</h3>

I converted both NLLB and Whisper to onnx format and quantized them in int8 (excluding some weights to ensure almost zero quality loss), I also separated some parts of the models to reduce RAM consumption (without this separation some weights were duplicated at runtime consuming more RAM than expected) and done other optimizations to reduce execution time.

Here are the results of my optimizations:

|         | normal NLLB onnx model <br />(full int8, no kv-cache)  | RTranslator NLLB onnx model <br /> (partial int8, with kv-cache, separated parts)  |
|---------| ------------------------------------------- | ---------------------------------------------------------- |
|RAM Consumption| 2.5 GB  | 1.3GB &nbsp;&nbsp;(1.9x improvement)  |
|Execution time for 75 tokens| 8s  | 2s &nbsp;&nbsp;(4x improvement)  |

|         | Whisper onnx model optimized with [Olive](https://github.com/microsoft/Olive) <br /> (full int8, with kv-cache)  | RTranslator Whisper onnx model <br /> (partial int8, with kv-cache, separated parts)  |
|---------| -------------------------------------------------------------- | ------------------------------------------------------------- |
|RAM Consumption| 1.4 GB  | 0.9 GB &nbsp;&nbsp;(1.5x improvement)|
|Execution time for 11s audio| 1.9s  | 1.6s &nbsp;&nbsp;(1.2x improvement)|

**N.B.** RTranslator Whisper model can also consume 0.5 GB of RAM but with an execution time of 2.1s (this mode is used for phones with less than 8 GB of RAM)
<br /><br />

<h3>Donations</h3>

This is an open-source and completely ad-free app, I don't make any money from it.

So, if you like the app and want to say thank you and support the project, you can make a donation via PayPal by clicking on the button below (any amount is well accepted).

<a href='https://www.paypal.com/donate/?business=3VBKS3WC6AFHN&no_recurring=0&currency_code=EUR'><img alt='Donate' src='https://raw.githubusercontent.com/niedev/RTranslator/v2.00/images/Paypal.png' style="width: 190px; height: 80px;" /></a>

In case you will donate, or just live a star, thank you :heart:
<br /><br />

<h3>Bugs and problems</h3>
I remind you that the app is still in beta. The bugs found are the following:

- Sometimes the Bluetooth connection drops.

If you have found any bug please report it by opening an issue, or by writing an email to contact.niedev@gmail.com
<br /><br />

Enjoy your simultaneous translator.
