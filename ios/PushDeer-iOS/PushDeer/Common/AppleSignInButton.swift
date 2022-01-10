//
//  AppleSignInButton.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/30.
//

import Foundation
import UIKit
import SwiftUI
import AuthenticationServices

/// 封装一个 Apple 登录按钮, 模仿 SwiftUI 中 SignInWithAppleButton 的行为
///
/// 那么为什么不直接使用系统的呢? 问的好, SignInWithAppleButton 在 macOS 上点击会报错, 在 iOS 上倒是正常.
@MainActor struct AppleSignInButton: UIViewRepresentable {
  
  var type = ASAuthorizationAppleIDButton.ButtonType.signIn
  var style = ASAuthorizationAppleIDButton.Style.black
  let onRequest: (ASAuthorizationAppleIDRequest) -> Void
  let onCompletion: (Result<ASAuthorization, Error>) async -> Void
  
  func makeUIView(context: Context) -> ASAuthorizationAppleIDButton {
    let signInButton = ASAuthorizationAppleIDButton(type: type, style: style)
    let coordinator = AppleSignInCoordinator(onRequest: onRequest, onCompletion: onCompletion)
    signInButton.addAction(
      UIAction { action in
        coordinator.performRequests()
      },
      for: UIControl.Event.touchUpInside
    )
    return signInButton
  }
  
  func updateUIView(_ uiView: ASAuthorizationAppleIDButton, context: Context) {
    
  }
}

class AppleSignInCoordinator: NSObject, ASAuthorizationControllerDelegate {
  
  let onRequest: (ASAuthorizationAppleIDRequest) -> Void
  let onCompletion: (Result<ASAuthorization, Error>) async -> Void
  
  init(
    onRequest: @escaping (ASAuthorizationAppleIDRequest) -> Void,
    onCompletion: @escaping (Result<ASAuthorization, Error>) async -> Void
  ) {
    self.onRequest = onRequest
    self.onCompletion = onCompletion
  }
  
  @objc func performRequests() {
    let appleIDProvider = ASAuthorizationAppleIDProvider()
    let request = appleIDProvider.createRequest()
    onRequest(request)
    let authorizationController = ASAuthorizationController(authorizationRequests: [request])
    authorizationController.delegate = self
    authorizationController.performRequests()
  }
  
  func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization) {
    print(authorization.debugDescription)
    Task {
      await onCompletion(.success(authorization))
    }
  }
  
  func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
    print(error.localizedDescription)
    Task {
      await onCompletion(.failure(error))
    }
  }
}
