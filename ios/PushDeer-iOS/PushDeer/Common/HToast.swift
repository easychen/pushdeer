//
//  HToast.swift
//  LotusDesktop
//
//  Created by HEXT on 2020/9/26.
//  Copyright © 2020 HEXT. All rights reserved.
//

import UIKit
import KRProgressHUD

struct HToast {
    static func showText(_ msg: String) {
        KRProgressHUD.showMessage(msg)
    }
    static func showSuccess(_ msg: String?) {
        KRProgressHUD.showSuccess(withMessage: msg)
    }
    static func showError(_ msg: String?) {
        KRProgressHUD.showError(withMessage: msg)
    }
    
    static func showLoading(_ msg: String?) {
        KRProgressHUD.show(withMessage: msg)
    }
    static func dismiss() {
        KRProgressHUD.dismiss()
    }
}
