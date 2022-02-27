# Uncomment the next line to define a global platform for your project
platform :ios, '14.0'

load 'remove_unsupported_libraries.rb'

# Comment the next line if you don't want to use dynamic frameworks
use_frameworks!

def commonPods
  # Pods for common
  pod 'Moya', '~> 15.0'
  pod 'SDWebImageSwiftUI', '~> 2.0.2'
  pod 'KRProgressHUD', '~> 3.4.7'
  pod 'IQKeyboardManagerSwift', '~> 6.5.9'
end

target 'PushDeer' do
  commonPods
  # Pods for PushDeer
  # PushDeer 独享的依赖, Clip 不支持
  pod 'WechatOpenSDK', '~> 1.8.7.1'
  pod 'WoodPeckeriOS', :configurations => ['Debug']
end

target 'PushDeerClip' do
  commonPods
  # Pods for PushDeerClip
  
end

# define unsupported pods
def unsupported_pods
  # macCatalyst 不支持的库
  ['WoodPeckeriOS', 'WechatOpenSDK']
end

def supported_pods
  # macCatalyst 支持的库
  ['Moya', 'SDWebImageSwiftUI', 'KRProgressHUD', 'IQKeyboardManagerSwift']
end

# install all pods except unsupported ones
post_install do |installer|
  $verbose = false # remove or set to false to avoid printing
  installer.configure_support_catalyst(supported_pods, unsupported_pods)
end
