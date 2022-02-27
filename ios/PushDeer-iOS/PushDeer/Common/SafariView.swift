//
//  SafariView.swift
//  PushDeer
//
//  Created by HEXT on 2022/2/27.
//

import SwiftUI
import SafariServices

struct SafariView: UIViewControllerRepresentable {
  
  let url: URL
  
  func makeUIViewController(context: Context) -> SFSafariViewController {
    let safariVC = SFSafariViewController.init(url: url)
    safariVC.dismissButtonStyle = .close
    return safariVC
  }
  
  func updateUIViewController(_ uiViewController: SFSafariViewController, context: Context) {
    // update code
  }
}
