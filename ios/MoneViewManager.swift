import UIKit
import Foundation

@objc(MoneViewManager)
class MoneViewManager: RCTViewManager {

  override func view() -> UIView! {
    return MoneView()
  }

  @objc override static func requiresMainQueueSetup() -> Bool {
    return false
  }
}
