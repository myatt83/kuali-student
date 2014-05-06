#$test_site = "http://localhost:8081/ks-with-rice-bundled-dev" #Local Env
$test_site = "http://env2.ks.kuali.org"
$test_site = ENV['TEST_SITE'] unless ENV['TEST_SITE'] == nil

$: << File.dirname(__FILE__)+'/../../lib'

require 'sambal-kuali'
require 'watir-webdriver/extensions/alerts' # required to deal with BROWSER-alerts (as opposed to JS-alerts)

World Foundry
World StringFactory
World DateFactory
World Workflows

client = Selenium::WebDriver::Remote::Http::Default.new
#client.timeout = 15 # seconds default is 60

Selenium::WebDriver::Firefox::Binary.path = ENV['FIREFOX_PATH'] unless ENV['FIREFOX_PATH'].nil?

browser = nil
headless = nil

#Profile Proxy Configuration
#profile = Selenium::WebDriver::Firefox::Profile.new
#profile.proxy = Selenium::WebDriver::Proxy.new :http => 'localhost:8001'
#, :profile => profile

if ENV['HEADLESS']
  require 'headless'
  headless = Headless.new :destroy_at_exit => false
  headless.start

  #to avoid nil browser error, retry initial browser connect in headless env
  retry_ctr = 0
  while browser.nil? and retry_ctr < 10
    retry_ctr += 1
    puts "debug env.rb - browser init - attempt #{retry_ctr}"
    begin
      browser = Watir::Browser.new :firefox, :http_client => client
      browser.goto("#{$test_site}/login.jsp")
      sleep 2
      raise "connect failed" unless  browser.text_field(id: "j_username").exists?
      #browser.goto('https://www.whatismybrowser.com/')
      #puts browser.div(class: 'simple-browser-string').text
      browser.close
      puts "debug env.rb - success: browser connection attempt #{retry_ctr}"
    rescue
      puts "debug env.rb - connect failed"
      browser.close unless browser.nil?
      browser = nil
      sleep 30
    end
  end
  browser = nil
end

#re-use browser for each scenario
Before do
  if browser == nil
    puts "debug  env.rb - creating new browser"
    browser = Watir::Browser.new :firefox, :http_client => client
    puts "debug  env.rb - browser.nil? #{browser.nil?}"
  end
  @browser = browser
end

at_exit { browser.close unless browser == nil }

if ENV['HEADLESS']
  # commented out to allow parallel execution
  #at_exit do
  #  headless.destroy
  #end

  #re-start browser after each failed scenario
  After do | scenario |
    if scenario.failed?
      #encoded_img = @browser.driver.screenshot_as(:base64)
      #embed("data:image/png;base64,#{encoded_img}",'image/png')
      @browser.close unless @browser == nil
      browser = nil
    end
  end
end