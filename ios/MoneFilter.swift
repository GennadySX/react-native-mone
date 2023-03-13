//
//  MoneFilter.swift
//  Mone
//
//  Created by Gennady Sabirovsky on 13.03.2023.
//  Copyright © 2023 Facebook. All rights reserved.
//

import Foundation
import UIKit

@objc(MoneFilter)
class MoneFilter: NSObject, RCTBridgeModule {

    static func requiresMainQueueSetup() -> Bool {
        return true
    }

    static func moduleName() -> String! {
        return "MoneFilter"
    }

    @objc func filterImage(_ imageUri: NSString, _ filterType: NSString, _ callback: RCTResponseSenderBlock) {
        let url = URL(string: String(imageUri))
        let data = try? Data(contentsOf: url!)
        let image = UIImage(data: data!)
        var filteredImage: UIImage?
        switch filterType {
        case "sepia":
            filteredImage = image?.filterSepia()
        case "blur":
            filteredImage = image?.filterBlur()
        case "polaroid":
            filteredImage = image?.filterPolaris()
        case "grayscale":
            filteredImage = image?.filterGrayScale()
        default:
            filteredImage = image
        }
        let base64String = filteredImage?.pngData()?.base64EncodedString(options: .lineLength64Characters)
      callback([base64String ?? ""])
    }


    @objc func getFilters(_ promise: RCTResponseSenderBlock, rejected: RCTPromiseRejectBlock) {
        let filters = ["sepia", "blur", "polaroid"]
        promise(filters)
    }

}
