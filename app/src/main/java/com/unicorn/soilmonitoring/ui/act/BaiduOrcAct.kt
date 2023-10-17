package com.unicorn.soilmonitoring.ui.act

import android.content.Intent
import androidx.viewbinding.ViewBinding
import com.baidu.FileUtil
import com.baidu.RecognizeService
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.baidu.ocr.ui.camera.CameraActivity
import com.baidu.ocr.ui.camera.CameraNativeHelper
import com.baidu.ocr.ui.camera.CameraView
import com.blankj.utilcode.util.ToastUtils
import com.unicorn.soilmonitoring.ui.base.BaseAct
import java.io.File

abstract class BaiduOrcAct<VB : ViewBinding>: BaseAct<VB>() {

    abstract fun onOrcResult(result: String)

    private var ocrPrepared = false

    override fun initIntents() {
        initAccessToken()
    }
    private fun initAccessToken() {
        OCR.getInstance(applicationContext).initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                ocrPrepared = true
//                initCameraNativeHelper()
            }

            override fun onError(error: OCRError) {
                "OCR初始化失败"
                // do nothing
            }
        }, applicationContext)
    }

    private fun initCameraNativeHelper() {
        //  初始化本地质量控制模型,释放代码在onDestroy中
        //  调用身份证扫描必须加上 intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true); 关闭自动初始化和释放本地模型
        CameraNativeHelper.init(
            this, OCR.getInstance(this).license
        ) { errorCode, _ ->
            ocrPrepared = false
            val msg: String = when (errorCode) {
                CameraView.NATIVE_SOLOAD_FAIL -> "加载so失败，请确保apk中存在ui部分的so"
                CameraView.NATIVE_AUTH_FAIL -> "授权本地质量控制token获取失败"
                CameraView.NATIVE_INIT_FAIL -> "本地质量控制"
                else -> errorCode.toString()
            }
            ToastUtils.showShort(msg)
        }
    }

    override fun onDestroy() {
        // 释放本地质量控制模型
        CameraNativeHelper.release()
        super.onDestroy()
    }

    @Suppress("DEPRECATION")
    fun startOrc() {
        if (ocrPrepared.not()) {
            ToastUtils.showShort("OCR未准备好")
            return
        }

        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra(
            CameraActivity.KEY_OUTPUT_FILE_PATH,
            FileUtil.getSaveFile(applicationContext).absolutePath
        )
        intent.putExtra(
            CameraActivity.KEY_CONTENT_TYPE,
            CameraActivity.CONTENT_TYPE_GENERAL
        )
        startActivityForResult(intent, 2333)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2333 && resultCode == RESULT_OK) {
            RecognizeService.recGeneralBasic(
                applicationContext, FileUtil.getSaveFile(applicationContext).absolutePath
            ) {
                onOrcResult(it)
            }
        }
    }

//    private fun recIDCard(filePath: String) {
//        val param = IDCardParams()
//        param.imageFile = File(filePath)
//        // 设置身份证正反面
//        param.idCardSide = IDCardParams.ID_CARD_SIDE_FRONT
//        // 设置方向检测
//        param.isDetectDirection = true
//        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
//        param.imageQuality = 20
//        param.setDetectRisk(true)
//        OCR.getInstance(this).recognizeIDCard(param, object : OnResultListener<IDCardResult?> {
//            override fun onResult(result: IDCardResult?) {
//                if (result != null) {
//                    onOrcResult(result)
//                }
//            }
//
//            override fun onError(error: OCRError) {
//                // do nothing
//            }
//        })
//    }

}