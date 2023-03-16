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

    @objc
    func filterImage(_ uri: String, filter: String, callback: @escaping RCTResponseSenderBlock) {
       DispatchQueue.global(qos: .background).async {
          let url = URL(string: String(uri))
          let data = try? Data(contentsOf: url!)
          let image = UIImage(data: data!)
          var filteredImage: UIImage?
          let filterLowerCase = filter.lowercased()
          switch filterLowerCase {
            case "sepia":
              filteredImage = image?.filter(filter: .sepia)
            case "blur":
              filteredImage = image?.filter(filter: .blur)
            case "grayscale":
              filteredImage = image?.filter(filter: .grayscale)

            case "sharpen":
                 filteredImage = image?.filter(filter: .sharpen)
            case "invert":
                 filteredImage = image?.filter(filter: .blackAndWhite)
            default:
              filteredImage = image
          }

         let base64String = filteredImage?.pngData()?.base64EncodedString(options: .lineLength64Characters)

         DispatchQueue.main.async {
               callback([base64String ?? ""])
             }
         }
     }
}
